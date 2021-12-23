import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Test {
    public static void main(String[] args) {
        Flux<String> routeId = Flux.just("123");
        routeId.flatMap((id) -> {
            System.out.println("nihao");
            return Mono.empty();
        }).subscribe();

        String a = "/a";
        String[] st = a.split("/");

    }
}
