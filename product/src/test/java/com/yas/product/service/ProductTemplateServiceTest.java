package com.yas.product.service;

import com.yas.product.ProductApplication;
import com.yas.product.exception.BadRequestException;
import com.yas.product.exception.DuplicatedException;
import com.yas.product.exception.NotFoundException;
import com.yas.product.model.attribute.ProductAttribute;
import com.yas.product.model.attribute.ProductAttributeTemplate;
import com.yas.product.model.attribute.ProductTemplate;
import com.yas.product.repository.ProductAttributeGroupRepository;
import com.yas.product.repository.ProductAttributeRepository;
import com.yas.product.repository.ProductAttributeTemplateRepository;
import com.yas.product.repository.ProductTemplateRepository;
import com.yas.product.utils.Constants;
import com.yas.product.viewmodel.producttemplate.ProductAttributeTemplatePostVm;
import com.yas.product.viewmodel.producttemplate.ProductTemplateListGetVm;
import com.yas.product.viewmodel.producttemplate.ProductTemplatePostVm;
import com.yas.product.viewmodel.producttemplate.ProductTemplateVm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductApplication.class)
class ProductTemplateServiceTest {
    @Autowired
    private ProductTemplateService productTemplateService;
    @Autowired
    private ProductAttributeRepository productAttributeRepository;
    @Autowired
    private ProductAttributeTemplateRepository productAttributeTemplateRepository;
    @Autowired
    private ProductAttributeGroupRepository productAttributeGroupRepository;
    @Autowired
    private ProductTemplateRepository productTemplateRepository;

    ProductAttribute productAttribute1;
    ProductAttribute productAttribute2;
    List<ProductAttribute> productAttributes;

    ProductAttributeTemplate productAttributeTemplate1;
    ProductAttributeTemplate productAttributeTemplate2;
    ProductTemplate productTemplate1;
    ProductTemplate productTemplate2;

    @BeforeEach
    void setUp(){
        productAttribute1 = ProductAttribute.builder().name("productAttribute1").build();
        productAttribute1 = productAttributeRepository.save(productAttribute1);
        productAttribute2 = ProductAttribute.builder().name("productAttribute2").build();
        productAttribute2 = productAttributeRepository.save(productAttribute2);
        productAttributes = List.of(productAttribute1, productAttribute2);

        productTemplate1 = ProductTemplate.builder().name("productTemplate1").build();
        productTemplate1 = productTemplateRepository.save(productTemplate1);
        productTemplate2 = ProductTemplate.builder().name("productTemplate2").build();
        productTemplate2 = productTemplateRepository.save(productTemplate2);

        productAttributeTemplate1 = ProductAttributeTemplate.builder()
                .productTemplate(productTemplate1)
                .productAttribute(productAttribute1)
                .build();
        productAttributeTemplate2 = ProductAttributeTemplate.builder()
                .productTemplate(productTemplate2)
                .productAttribute(productAttribute2)
                .build();
        productAttributeTemplateRepository.saveAll(
                List.of(productAttributeTemplate1, productAttributeTemplate2));
    }

    @AfterEach
    void tearDown(){
        productAttributeTemplateRepository.deleteAll();
        productAttributeRepository.deleteAll();
        productTemplateRepository.deleteAll();
    }

    @Test
    void getPageableProductTemplate_WhenGetPageable_thenSuccess(){
        int pageNo = 0;
        int pageSize = 10;
        int totalElement = 2;
        int totalPages = 1;
        ProductTemplateListGetVm actualResponse = productTemplateService.getPageableProductTemplate(pageNo,pageSize);
        assertEquals(true, actualResponse.isLast());
        assertEquals(totalPages, actualResponse.totalPages());
        assertEquals(pageNo, actualResponse.pageNo());
        assertEquals(pageSize, actualResponse.pageSize());
        assertEquals(totalElement, actualResponse.productTemplateVms().size());

    }
    @Test
    void getProductTemplate_WhenIdProductTemplateNotExit_ThrowsNotFoundException() {
        Long invalidId = 9999L;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productTemplateService.getProductTemplate(invalidId));
        assertEquals(Constants.ERROR_CODE.PRODUCT_TEMPlATE_IS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getProductTemplate_WhenIdProductTemplateValid_thenSuccess(){
        ProductTemplate productTemplateDB = productTemplateRepository.findAll().get(0);
        ProductTemplateVm actualResponse = productTemplateService.getProductTemplate(productTemplateDB.getId());
        assertEquals(productTemplateDB.getId(), actualResponse.id());
        assertEquals(productTemplateDB.getName(), actualResponse.name());
        assertEquals(1, actualResponse.productAttributeTemplates().size());
    }

    @Test
    void saveProductTemplate_WhenDuplicateName_ThenThrowDuplicatedException(){
        ProductTemplatePostVm productTemplatePostVm = new ProductTemplatePostVm("productTemplate1",null);
        DuplicatedException exception = assertThrows(DuplicatedException.class, () -> productTemplateService.saveProductTemplate(productTemplatePostVm));
        assertEquals("Request name productTemplate1 is already existed", exception.getMessage());

    }
    @Test
    void saveProductTemplate_WhenProductAttributesNotFound_ThenThrowBadRequestException(){
        List<ProductAttributeTemplatePostVm>  listProductAttTemplates = new ArrayList<>();
        listProductAttTemplates.add(new ProductAttributeTemplatePostVm(9999L,0));
        ProductTemplatePostVm productTemplatePostVm = new ProductTemplatePostVm("productTemplate3",listProductAttTemplates);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> productTemplateService.saveProductTemplate(productTemplatePostVm));
        assertThat(exception.getMessage()).isEqualTo(Constants.ERROR_CODE.PRODUCT_ATTRIBUTE_NOT_FOUND);
    }

    @Test
    void saveProductTemplate_WhenProductTemplatePostVm_ThenSuccess(){
        ProductTemplatePostVm productTemplatePostVm = new ProductTemplatePostVm("productTemplate3",
                List.of(new ProductAttributeTemplatePostVm(productAttribute1.getId(), 0)));
        ProductTemplateVm productTemplateVmDB = productTemplateService.saveProductTemplate(productTemplatePostVm);
        Optional<ProductTemplate> productTemplate = productTemplateRepository.findById(productTemplateVmDB.id());
        assertTrue(productTemplate.isPresent());
        assertEquals(productTemplatePostVm.name(), productTemplate.get().getName());
    }

    @Test
    void updateProductTemplate_WhenIdProductTemplateNotExist_ThenThrowNotFoundException(){
        ProductTemplatePostVm productTemplatePostVm = new ProductTemplatePostVm("productTemplate2",
                List.of(new ProductAttributeTemplatePostVm(productAttribute1.getId(), 0)));
        NotFoundException exception = assertThrows(NotFoundException.class, () -> productTemplateService.updateProductTemplate(9999L, productTemplatePostVm));
        assertEquals(Constants.ERROR_CODE.PRODUCT_TEMPlATE_IS_NOT_FOUND, exception.getMessage());
    }

}
