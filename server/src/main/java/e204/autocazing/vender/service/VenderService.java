package e204.autocazing.vender.service;

import e204.autocazing.db.entity.StoreEntity;
import e204.autocazing.db.entity.VenderEntity;
import e204.autocazing.db.repository.StoreRepository;
import e204.autocazing.db.repository.VenderRepository;
import e204.autocazing.vender.dto.PatchVenderDto;
import e204.autocazing.vender.dto.PostVenderDto;
import e204.autocazing.vender.dto.VenderDto;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenderService {
    @Autowired
    private VenderRepository venderRepository;
    @Autowired
    private StoreRepository storeRepository;


    public void createVender(PostVenderDto postVenderDto, String loginId) {
        VenderEntity venderEntity = new VenderEntity();
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
        Optional<StoreEntity> store = storeRepository.findById(storeId);

        if (!store.isPresent())
            throw new EntityNotFoundException("Store not found with id: " + storeId);

        venderEntity.setStore(store);
        venderEntity.setVenderName(postVenderDto.getVenderName());
        venderEntity.setVenderManager(postVenderDto.getVenderManager());
        venderEntity.setVenderManagerContact(postVenderDto.getVenderManagerContact());
        venderEntity.setVenderDescription(postVenderDto.getVenderDescription());
        venderRepository.save(venderEntity);
    }

    public VenderDto updateVender(Integer venderId, PatchVenderDto patchVenderDto) {
        VenderEntity vender = venderRepository.findById(venderId)
                .orElseThrow(() -> new IllegalArgumentException("Vender not found"));
        if (patchVenderDto.getVenderName() != null) vender.setVenderName(patchVenderDto.getVenderName());
        if (patchVenderDto.getVenderManager() != null) vender.setVenderManager(patchVenderDto.getVenderManager());
        if (patchVenderDto.getVenderManagerContact() != null) vender.setVenderManagerContact(patchVenderDto.getVenderManagerContact());
        if (patchVenderDto.getVenderDescription() != null) vender.setVenderDescription(patchVenderDto.getVenderDescription());
        venderRepository.save(vender);
        return fromEntity(vender);
    }

    public void deleteVender(Integer venderId) {
        venderRepository.deleteById(venderId);
    }

    public List<VenderDto> getAllVenders(String loginId) {
        Integer storeId = storeRepository.findStoreIdByLoginId(loginId);
        List<VenderEntity> venderEntityList = venderRepository.findAllByStoreId(storeId);

        List<VenderDto> venderDtoList = new ArrayList<>();
        for(VenderEntity vender : venderEntityList)
            venderDtoList.add(fromEntity(vender));

        return venderDtoList;
    }

    public VenderDto getVenderById(Integer venderId) {
        VenderEntity vender = venderRepository.findById(venderId)
                .orElseThrow(() -> new IllegalArgumentException("Vender not found with id: " + venderId));
        return fromEntity(vender);
    }

    private VenderDto fromEntity(VenderEntity vender) {
        return VenderDto.builder()
                .venderId(vender.getVenderId())
                .venderName(vender.getVenderName())
                .venderManager(vender.getVenderManager())
                .venderManagerContact(vender.getVenderManagerContact())
                .venderDescription(vender.getVenderDescription())
                .build();
    }
}
