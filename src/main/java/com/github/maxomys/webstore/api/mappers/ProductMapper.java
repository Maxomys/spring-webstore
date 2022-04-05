package com.github.maxomys.webstore.api.mappers;

import com.github.maxomys.webstore.api.dtos.ProductDto;
import com.github.maxomys.webstore.domain.Image;
import com.github.maxomys.webstore.domain.Inquiry;
import com.github.maxomys.webstore.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "images", target = "imageIds")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.description", target = "categoryName")
    @Mapping(source = "inquiries", target = "inquiryIds")
    ProductDto productToProductDto(Product product);


    Product productDtoToProduct(ProductDto productDto);

    default Long imageToImageId(Image image) {
        return image.getId();
    }

    default Long inquiryToInquiryId(Inquiry inquiry) {
        return inquiry.getId();
    }

}
