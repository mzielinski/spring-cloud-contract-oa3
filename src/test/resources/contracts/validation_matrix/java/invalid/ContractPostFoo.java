import java.util.function.Supplier;

import org.springframework.cloud.contract.spec.Contract;

public class ContractPostFoo implements Supplier<Contract> {

    @Override
    public Contract get() {
        return Contract.make(contract -> {
            contract.name("should_reject_post_foo_when_method_missing_in_openapi");
            contract.request(request -> {
                request.method("POST");
                request.urlPath("/foo");
            });
            contract.response(response -> response.status(200));
        });
    }
}
