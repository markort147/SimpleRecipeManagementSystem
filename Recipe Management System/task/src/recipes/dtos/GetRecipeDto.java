package recipes.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetRecipeDto {

    private String name;
    private String description;
    private String category;
    private List<String> ingredients;
    private List<String> directions;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime date;
}
