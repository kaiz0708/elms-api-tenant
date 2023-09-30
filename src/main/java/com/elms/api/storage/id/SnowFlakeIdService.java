package com.elms.api.storage.id;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.validator.constraints.Range;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public class SnowFlakeIdService {

    private static SnowFlakeIdService instance;



    // ============================== 30K unique ID / seconds===========================================
    /** Start time cut (2015-01-01) */
    private final long twepoch = 1489111610226L;


    /** The number of bits in the machine id  5 -> 6*/
    private final long workerIdBits = 5L;

    /** Number of digits in the data identifier id 5*/
    private final long dataCenterIdBits = 5L;

    /** The maximum machine id supported, the result is 31 (this shift algorithm can quickly calculate the maximum decimal number that can be represented by several binary digits) */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /** The maximum data ID id supported, the result is 31 */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /** The number of bits in the id of the sequence 12 -> 5*/
    private final long sequenceBits = 5L;

    /** Machine ID is shifted to the left by 12 digits */
    private final long workerIdShift = sequenceBits;

    /** The data identification id is shifted to the left by 17 digits (12+5) */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /** Time is shifted to the left by 22 bits (5+5+12) */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /** Generate a mask for the sequence, here 4095 (0b111111111111=0xfff=4095) */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /** Work Machine ID (0~31) */
    private long workerId;

    /** Data Center ID (0~31) */
    private long dataCenterId;

    /** Sequence within milliseconds (0~4095) */
    private long sequence = 0L;

    /** Time to last generated ID */
    private long lastTimestamp = -1L;



    // Create SequenceGenerator with a nodeId
    private SnowFlakeIdService() {

    }

    public synchronized static SnowFlakeIdService getInstance() {
        if(instance == null){
            instance = new SnowFlakeIdService();
        }
        return instance;
    }

    /** Work Machine ID (0~63) */
    public void setNodeId(@Range(min = 0, max = 63) int nodeId) {
        if(nodeId < 0 || nodeId > maxWorkerId) {
            throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, maxWorkerId));
        }
        this.workerId = nodeId;
    }

    /** Data Center ID (0~31) */
    public void setDataCenterId(@Range(min = 0, max = 32) int dataCenterId){
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.dataCenterId = dataCenterId;
    }


    // ==============================Methods==========================================
    /**
     * Get the next ID (this method is thread safe)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //If the current time is less than the timestamp generated by the previous ID, it means that the system clock should be thrown abnormally at this time.
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // If it is generated at the same time, then the sequence within milliseconds
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //Sequence overflow in milliseconds
            if (sequence == 0) {
                // Block to the next millisecond, get a new timestamp
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //Timestamp changed, sequence reset in milliseconds
        else {
            sequence = 0L;
        }
        // The last time the ID was generated
        lastTimestamp = timestamp;

        // Shift and put together by the OR operation to form a 64-bit ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * Block until the next millisecond until a new timestamp is obtained
     * @param lastTimestamp The timestamp of the last generated ID
     * @return current timestamp
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * Returns the current time in milliseconds
     * @return current time (ms)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    private static Long getWorkId(){
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for(int b : ints){
                sums += b;
            }
            return (long)(sums % 32);
        } catch (UnknownHostException e) {
            // If the acquisition fails, use a random number to reserve
            return RandomUtils.nextLong(0,31);
        }
    }

    private static Long getDataCenterId(){
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i: ints) {
            sums += i;
        }
        return (long)(sums % 32);
    }


//    public static void main(String[] args) {
//
//        SnowFlakeIdService.getInstance().setDataCenterId(0);
//        SnowFlakeIdService.getInstance().setNodeId(0);
//
//        System.out.println(SnowFlakeIdService.getInstance().nextId());
//        Map<Long, Integer> map = new HashMap<>();
//        long start = System.currentTimeMillis();
//        for(int i =0; i< 30000; i++){
//            Long next = SnowFlakeIdService.getInstance().nextId();
//            // safe int cua javascript: 9007199254740991L
//            if(next > 9007199254740991L){
//                System.out.println("out of size");
//            }
//            if(!map.containsKey(next)){
//                map.put(next,1);
//            }else{
//                Integer value = map.get(next);
//                map.put(next, value +1);
//                System.out.println("id: "+next +", total: "+value +1);
//            }
//        }
//        System.out.println("total time: "+((System.currentTimeMillis()-start))+", map leng: "+map.size());
//    }
}
