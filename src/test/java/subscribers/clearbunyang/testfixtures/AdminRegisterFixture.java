package subscribers.clearbunyang.testfixtures;


import java.util.ArrayList;
import java.util.List;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.entity.enums.AdminState;
import subscribers.clearbunyang.domain.auth.entity.enums.UserRole;
import subscribers.clearbunyang.global.file.entity.File;
import subscribers.clearbunyang.global.file.entity.enums.FileType;

public class AdminRegisterFixture {

    public static Admin createDefault() {
        List<File> files = new ArrayList<>();
        files.add(
                new File(
                        null,
                        null,
                        "property_image.jpg",
                        "https://example.com/property_image.jpg",
                        FileType.REGISTRATION));

        return Admin.builder()
                .name("Default Admin")
                .email("admin@example.com")
                .password("defaultPassword123!")
                .phoneNumber("01012345678")
                .companyName("Default Company")
                .address("Default Address")
                .business("Default Business")
                .status(AdminState.ACCEPTED)
                .role(UserRole.ADMIN)
                .files(files)
                .build();
    }
}
