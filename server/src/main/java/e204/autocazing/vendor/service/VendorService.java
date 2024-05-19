package e204.autocazing.vendor.service;

import e204.autocazing.db.entity.VendorEntity;
import e204.autocazing.db.repository.VendorRepository;
import e204.autocazing.vendor.dto.PatchVendorDto;
import e204.autocazing.vendor.dto.PostVendorDto;
import e204.autocazing.vendor.dto.VendorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    public void createVendor(PostVendorDto postVendorDto) {
        VendorEntity vendorEntity = new VendorEntity();
        vendorEntity.setVendorName(postVendorDto.getVendorName());
        vendorEntity.setVendorManager(postVendorDto.getVendorManager());
        vendorEntity.setVendorManagerContact(postVendorDto.getVendorManagerContact());
        vendorEntity.setVendorDescription(postVendorDto.getVendorDescription());
        vendorRepository.save(vendorEntity);

    }

    public VendorDto updateVendor(Integer vendorId, PatchVendorDto patchVendorDto) {
        VendorEntity vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
        if (patchVendorDto.getVendorName() != null) vendor.setVendorName(patchVendorDto.getVendorName());
        if (patchVendorDto.getVendorManager() != null) vendor.setVendorManager(patchVendorDto.getVendorManager());
        if (patchVendorDto.getVendorManagerContact() != null) vendor.setVendorManagerContact(patchVendorDto.getVendorManagerContact());
        if (patchVendorDto.getVendorDescription() != null) vendor.setVendorDescription(patchVendorDto.getVendorDescription());
        vendorRepository.save(vendor);
        return fromEntity(vendor);
    }

    public void deleteVendor(Integer vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    public List<VendorDto> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public VendorDto getVendorById(Integer vendorId) {
        VendorEntity vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));
        return fromEntity(vendor);
    }

    private VendorDto fromEntity(VendorEntity vendor) {
        return VendorDto.builder()
                .vendorId(vendor.getVendorId())
                .vendorName(vendor.getVendorName())
                .vendorManager(vendor.getVendorManager())
                .vendorManagerContact(vendor.getVendorManagerContact())
                .vendorDescription(vendor.getVendorDescription())
                .build();
    }
}
