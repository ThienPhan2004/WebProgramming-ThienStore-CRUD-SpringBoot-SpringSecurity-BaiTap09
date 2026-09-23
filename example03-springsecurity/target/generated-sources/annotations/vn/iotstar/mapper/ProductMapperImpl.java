package vn.iotstar.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-22T17:15:40+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260528-0407, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDTO(Product entity) {
        if ( entity == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setUserId( entityUserId( entity ) );
        productDTO.setUsername( entityUserUsername( entity ) );
        productDTO.setId( entity.getId() );
        productDTO.setName( entity.getName() );
        productDTO.setDescription( entity.getDescription() );
        productDTO.setPrice( entity.getPrice() );
        productDTO.setImageUrl( entity.getImageUrl() );

        return productDTO;
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product.Builder product = Product.builder();

        product.id( dto.getId() );
        product.name( dto.getName() );
        product.description( dto.getDescription() );
        product.price( dto.getPrice() );
        product.imageUrl( dto.getImageUrl() );

        return product.build();
    }

    private Long entityUserId(Product product) {
        User user = product.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String entityUserUsername(Product product) {
        User user = product.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }
}
