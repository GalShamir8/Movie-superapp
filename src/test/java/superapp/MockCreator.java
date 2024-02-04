package superapp;

import superapp.fixture.Generator;
import superapp.models.CommandId;
import superapp.models.InvokedBy;
import superapp.models.TargetObject;
import superapp.models.UserId;
import superapp.requestModels.ObjectBoundaryModel;
import superapp.requestModels.UserBoundaryModel;
import superapp.view.MiniAppCommandBoundary;
import java.util.HashMap;
import java.util.Random;

public class MockCreator {
        public static UserBoundaryModel buildMockAdminRequestModel() {
            new UserBoundaryModel();
            return new UserBoundaryModel(
                    "admin@test.com",
                    "testedAdmin",
                    "testedAdmin",
                    "ADMIN",
                    new HashMap<>()
            );
        }

        public static UserBoundaryModel buildMockUserRequestModel() {
            new UserBoundaryModel();
            return new UserBoundaryModel(
                    Generator.generateEmail(),
                    Generator.generateString(),
                    Generator.generateString(),
                    Generator.generateUserRole().name(),
                    new HashMap<>()
            );
        }

        public static MiniAppCommandBoundary buildMockMiniAppCommandBoundary() {
            new MiniAppCommandBoundary();
            return new MiniAppCommandBoundary(
                    new CommandId(),
                    Generator.generateString(),
                    new TargetObject(),
                    Generator.generateDateTime(),
                    new InvokedBy(),
                    new HashMap<>()
            );
        }
        public static UserBoundaryModel buildMockSuperappUserModel() {
            new UserBoundaryModel();
            return new UserBoundaryModel(
                    "superappUser@test.com",
                    "superappUser",
                    "superappUser",
                    "SUPERAPP_USER",
                    new HashMap<>()
            );
        }

        public static ObjectBoundaryModel buildMockSuperAppObjectRequestModel() {
            return (new ObjectBoundaryModel(
                    "movie",
                    Generator.generateString(),
                    new Random().nextBoolean(),
                    new UserId("2023.ohad.saada","superappUser@test.com"),
                    new HashMap<>()
            ));
        }

    }




