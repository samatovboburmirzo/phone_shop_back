package phone.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Getter
@Setter
public class FilterDTO {
    private Integer page;
    private Integer size;
    private String sortBy;
    private Sort.Direction direction;
}
