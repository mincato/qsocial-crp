package com.qsocialnow.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.config.AWSLambdaClientConfig;
import com.qsocialnow.config.CacheConfig;
import com.qsocialnow.model.OrganizationId;
import com.qsocialnow.model.User;

public class UserRepositoryImpl implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Value("${app.client}")
    private Integer clientId;

    @Override
    @Cacheable(value = CacheConfig.USERS, unless = "#result == null")
    public List<UserListView> findAll() {

        log.info("Searching users on external service");

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
