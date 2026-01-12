import java.util.function.Supplier;

import org.springframework.cloud.contract.spec.Contract;

public class ContractGetFooBadStatus implements Supplier<Contract> {

    @Override
    public Contract get() {
        return Contract.make(contract -> {
            contract.name("should_reject_get_foo_when_status_missing_in_openapi");
            contract.request(request -> {
                request.method("GET");
                request.urlPath("/foo");
            });
            contract.response(response -> response.status(201));
        });
    }
}
