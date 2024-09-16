package subscribers.clearbunyang.testfixtures;


import java.util.ArrayList;
import java.util.List;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.enums.AdminState;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;

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
