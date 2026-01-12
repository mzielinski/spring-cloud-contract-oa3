import java.util.function.Supplier;

import org.springframework.cloud.contract.spec.Contract;

public class ContractPostBar implements Supplier<Contract> {

    @Override
    public Contract get() {
        return Contract.make(contract -> {
            contract.name("should_accept_post_bar_when_path_parameter_matches");
            contract.request(request -> {
                request.method("POST");
                request.urlPath("/bar/123");
            });
            contract.response(response -> response.status(201));
        });
    }
}
