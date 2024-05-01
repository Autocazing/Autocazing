package e204.autocazing.vendor.controller;

import e204.autocazing.db.entity.VendorEntity;
import e204.autocazing.vendor.dto.PatchVendorDto;
import e204.autocazing.vendor.dto.PostVendorDto;
import e204.autocazing.vendor.dto.VendorDto;
import e204.autocazing.vendor.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vendors")
public class VendorController {
    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity createVendor(@RequestBody PostVendorDto postVendorDto) {
         vendorService.createVendor(postVendorDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PatchMapping("/{vendorId}")
    public ResponseEntity updateVendor(@PathVariable(name = "vendorId") Integer vendorId, @RequestBody PatchVendorDto patchVendorDto) {
        VendorDto vendorDto = vendorService.updateVendor(vendorId, patchVendorDto);
        return ResponseEntity.ok(vendorDto);
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity deleteVendor(@PathVariable(name = "vendorId") Integer vendorId) {
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable(name = "vendorId") Integer vendorId) {
        VendorDto vendor = vendorService.getVendorById(vendorId);
        return ResponseEntity.ok(vendor);
    }

    @GetMapping
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }
}
