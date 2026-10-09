package com.gymmanagement.service;

import com.gymmanagement.model.Equipment;
import com.gymmanagement.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public List<Equipment> getEquipmentByCategory(String category) {
        return equipmentRepository.findByCategory(category);
    }

    public Equipment getEquipmentById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));
    }

    public Equipment createEquipment(Equipment equipment) {
        if (equipment.getLastMaintenanceDate() == null) {
            equipment.setLastMaintenanceDate(LocalDate.now());
        }
        return equipmentRepository.save(equipment);
    }

    public Equipment updateEquipment(Long id, Equipment details) {
        Equipment equipment = getEquipmentById(id);
        equipment.setName(details.getName());
        equipment.setCategory(details.getCategory());
        equipment.setQuantity(details.getQuantity());
        equipment.setConditionStatus(details.getConditionStatus());
        equipment.setLastMaintenanceDate(details.getLastMaintenanceDate());
        equipment.setPurchaseDate(details.getPurchaseDate());
        equipment.setLocation(details.getLocation());
        return equipmentRepository.save(equipment);
    }

    public Equipment markMaintenanceDone(Long id) {
        Equipment equipment = getEquipmentById(id);
        equipment.setConditionStatus("GOOD");
        equipment.setLastMaintenanceDate(LocalDate.now());
        return equipmentRepository.save(equipment);
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }
}
