package com.bookshop.sachservice.event;

import com.bookshop.sachservice.dto.OrderPlacementEvent;
import com.bookshop.sachservice.model.Sach;
import com.bookshop.sachservice.repository.SachRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KafkaConsumer {
    private SachRepository sachRepository;
    @Autowired
    public KafkaConsumer(SachRepository sachRepository) {
        this.sachRepository = sachRepository;
    }

    @KafkaListener(
            topics = "${constant.kafka.order-placed}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"value.deserializer:com.bookshop.sachservice.event.OrderPlacementValueDeserializer"}
    )
    public void consumeOrderPlacementEvent(ConsumerRecord<String, List<OrderPlacementEvent>> record) {
        List<OrderPlacementEvent> sachs = record.value();
        Map<Long, Integer> soLuongBySachId = sachs.stream().collect(Collectors.toMap(OrderPlacementEvent::getSachId, OrderPlacementEvent::getSoLuong));
        List<Long> idList = sachs.stream().map(OrderPlacementEvent::getSachId).toList();
        List<Sach> result = sachRepository.findAllById(idList);
        result.forEach(sach -> {
            Integer soLuongGiam = soLuongBySachId.get(sach.getId());
            sach.setSoLuong(sach.getSoLuong() - soLuongGiam);
        });
        sachRepository.saveAll(result);
        System.out.println("Cập nhật số lượng thành công");
    }
}
