package com.bookshop.sachservice.event;

import com.bookshop.sachservice.dto.OrderPlacementEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class OrderPlacementValueDeserializer implements Deserializer<List<OrderPlacementEvent>> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
//        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public List<OrderPlacementEvent> deserialize(String s, byte[] bytes) {
        try {
            System.out.println("Deserializing...");
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonNode = objectMapper.readTree(bytes);
//            Map<String, List<OrderPlacementEvent>> result = new HashMap<>();
//            BytesSerializer
            Iterator<Map.Entry<String, JsonNode>> keys = jsonNode.fields();
            List<OrderPlacementEvent> list = null;
            while(keys.hasNext()) {
                String key = keys.next().getKey();
                String listAsString = jsonNode.at("/"+key).toString();
                System.out.println(key);
                list = Arrays.stream(objectMapper.readValue(listAsString, OrderPlacementEvent[].class)).toList();
//                result.put(key, list);
            }
            return list;
//            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), Map.class);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {
//        Deserializer.super.close();
    }
}
