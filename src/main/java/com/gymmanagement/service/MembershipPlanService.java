package com.gymmanagement.service;

import com.gymmanagement.model.MembershipPlan;
import com.gymmanagement.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipPlanService {

    private final MembershipPlanRepository planRepository;

    public List<MembershipPlan> getAllPlans() {
        return planRepository.findAll();
    }

    public List<MembershipPlan> getActivePlans() {
        return planRepository.findByActiveTrue();
    }

    public MembershipPlan getPlanById(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership plan not found with id: " + id));
    }

    public MembershipPlan createPlan(MembershipPlan plan) {
        return planRepository.save(plan);
    }

    public MembershipPlan updatePlan(Long id, MembershipPlan details) {
        MembershipPlan plan = getPlanById(id);
        plan.setName(details.getName());
        plan.setDescription(details.getDescription());
        plan.setDurationMonths(details.getDurationMonths());
        plan.setPrice(details.getPrice());
        plan.setFeatures(details.getFeatures());
        plan.setActive(details.getActive());
        return planRepository.save(plan);
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }
}
