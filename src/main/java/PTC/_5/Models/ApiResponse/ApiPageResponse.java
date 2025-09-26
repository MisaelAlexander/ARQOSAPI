package PTC._5.Models.ApiResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ApiPageResponse<T>
{
 private List<T> content;
 private int pageNumber;
 private int pageSize;
 private long totalElements;
 private int totalPages;
 private boolean last;

    public ApiPageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}
