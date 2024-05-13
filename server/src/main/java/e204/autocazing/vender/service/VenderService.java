package e204.autocazing.vender.service;

import e204.autocazing.db.entity.VenderEntity;
import e204.autocazing.db.repository.VenderRepository;
import e204.autocazing.vender.dto.PatchVenderDto;
import e204.autocazing.vender.dto.PostVenderDto;
import e204.autocazing.vender.dto.VenderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenderService {
    @Autowired
    private VenderRepository vendorRepository;

    public void createVendor(PostVenderDto postVenderDto) {
        VenderEntity venderEntity = new VenderEntity();
        venderEntity.setVenderName(postVenderDto.getVendorName());
        venderEntity.setVenderManager(postVenderDto.getVendorManager());
        venderEntity.setVenderManagerContact(postVenderDto.getVendorManagerContact());
        venderEntity.setVenderDescription(postVenderDto.getVendorDescription());
        vendorRepository.save(venderEntity);

    }

    public VenderDto updateVendor(Integer vendorId, PatchVenderDto patchVenderDto) {
        VenderEntity vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
        if (patchVenderDto.getVendorName() != null) vendor.setVenderName(patchVenderDto.getVendorName());
        if (patchVenderDto.getVendorManager() != null) vendor.setVenderManager(patchVenderDto.getVendorManager());
        if (patchVenderDto.getVendorManagerContact() != null) vendor.setVenderManagerContact(patchVenderDto.getVendorManagerContact());
        if (patchVenderDto.getVendorDescription() != null) vendor.setVenderDescription(patchVenderDto.getVendorDescription());
        vendorRepository.save(vendor);
        return fromEntity(vendor);
    }

    public void deleteVendor(Integer vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    public List<VenderDto> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public VenderDto getVendorById(Integer vendorId) {
        VenderEntity vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));
        return fromEntity(vendor);
    }

    private VenderDto fromEntity(VenderEntity vendor) {
        return VenderDto.builder()
                .vendorId(vendor.getVenderId())
                .vendorName(vendor.getVenderName())
                .vendorManager(vendor.getVenderManager())
                .vendorManagerContact(vendor.getVenderManagerContact())
                .vendorDescription(vendor.getVenderDescription())
                .build();
    }
}
