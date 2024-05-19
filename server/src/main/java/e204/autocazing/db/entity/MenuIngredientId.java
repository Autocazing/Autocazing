package e204.autocazing.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuIngredientId implements Serializable {
    private Integer menu; // MenuEntity의 menuId에 해당
    private Integer ingredient; // IngredientEntity의 ingredientId에 해당


    // equals() 메소드 구현 -> 두 객체가 동일한지 비교
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuIngredientId that = (MenuIngredientId) o;
        return Objects.equals(menu, that.menu) && Objects.equals(ingredient, that.ingredient);
    }

    // hashCode() 메소드 구현 동일한 해시코드를 반환해야함.
    @Override
    public int hashCode() {
        return Objects.hash(menu, ingredient);
    }
}
