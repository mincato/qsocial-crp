package com.qsocialnow.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.config.AWSLambdaClientConfig;
import com.qsocialnow.model.OrganizationId;
import com.qsocialnow.model.User;

public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Value("${app.client}")
    private Integer clientId;

    @Override
    public List<UserListView> findAll() {
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        UsersByOrganizationService service = LambdaInvokerFactory.builder().lambdaClient(lambda)
                .build(UsersByOrganizationService.class);
        OrganizationId organizationId = new OrganizationId();
        organizationId.setClientOrganizationId(clientId);
        List<User> users = service.usersByClientOrganizationId(organizationId).getUsers();
        return users.stream().map(user -> {
            UserListView userListView = new UserListView();
            userListView.setId(user.getIdUsuario());
            userListView.setUsername(user.getNombreDeLogin());
            return userListView;
        }).collect(Collectors.toList());
    }

}
