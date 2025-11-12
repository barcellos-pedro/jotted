import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Article {
    String DEFAULT_FRONT_MATTER = """
            ---
            title: %s
            slug: %s
            description: %s
            author: %s
            date: %s
            tags:
              %s
            image: %s
            draft: %s
            template: %s
            ---
            """;

    String title();

    String slug();

    String description();

    String author();

    String date();

    String[] tags();

    String image();

    boolean draft() default false;

    String template();
}