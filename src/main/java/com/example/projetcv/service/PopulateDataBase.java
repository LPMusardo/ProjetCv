package com.example.projetcv.service;

import com.example.projetcv.dao.UserRepository;
import com.example.projetcv.model.Activity;
import com.example.projetcv.model.CV;
import com.example.projetcv.model.Nature;
import com.example.projetcv.model.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.apache.commons.lang3.StringUtils.stripAccents;

@Component
public class PopulateDataBase implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${com.example.projetcv.db.number-random-users:10000}")
    private int numberOfRandomUsers;

    private Random random = new Random(42);
    Faker faker = new Faker(new Locale("fr", "FR"), new Random(42));

    private Logger logger = Logger.getLogger(this.getClass().getName());


    //------------------------------------------------------------------

    @Override
    public void run(String... args) throws Exception {
        clearDb();
        generateCustomUsers();
        generateRandomUsers(numberOfRandomUsers);
        logger.info("Database populated");
    }

    private void clearDb() {
        userRepository.deleteAll();
    }

    private void generateRandomUsers(int numberOfUser) {
        String hash = passwordEncoder.encode("password");

        //List<User> users = new ArrayList<>();
        for (int i = 0; i < numberOfUser; i++) {
            String pFirstName = stripAccents(faker.name().firstName());
            String pLastName = stripAccents(faker.name().lastName());

            // required
            var user = User.builder().name(pFirstName).firstName(pLastName).birthday(LocalDate.ofYearDay(randomInt(1950,2001), randomInt(1,365)))
                    .email(faker.internet().emailAddress(pFirstName.toLowerCase() + i)).passwordHash(hash).website(pFirstName.toLowerCase() + "." + pLastName.toLowerCase() + ".com").build();
            // optional
            if (i % 2 == 0) {
                user.setRoles(Set.of("USER"));
            }
            if (i % 10 != 0) {
                CV cv = generateRandomCv(user);
                user.setCv(cv);
            }

            userRepository.save(user);
        }

    }

    private CV generateRandomCv(User user) {
        CV cv = CV.builder().user(user).build();
        int nbActivities = randomInt(1, 3);
        List<Activity> activities = new ArrayList<>(nbActivities);

        for (int i = 0; i < nbActivities; i++) {
            Activity activity = Activity.builder().cv(cv).year(randomInt(2000, 2023)).nature(Nature.randomNature()).title(faker.job().seniority() + " " + faker.job().position() + " " +faker.job().field() + " " + faker.job().keySkills()).build();
            if (random.nextBoolean()) activity.setWebAddress(faker.internet().url());
            if (random.nextBoolean()) activity.setDescription(faker.lorem().paragraph(randomInt(1, 3)));
            activities.add(activity);
        }
        cv.setActivities(activities);

        return cv;
    }


    private void generateCustomUsers() {
        User amdinLP = User.builder().firstName("lp").name("mu").birthday(LocalDate.now()).email("lpmusardo@gmail.com").roles(Set.of("ADMIN", "USER")).passwordHash(passwordEncoder.encode("password")).website("https://www.linkedin.com/in/lpmusardo/").build();
        User userMaxime = User.builder().firstName("maxime").name("gu").birthday(LocalDate.now()).email("maxime@gmail.com").roles(Set.of("USER")).passwordHash(passwordEncoder.encode("password")).website("https://www.linkedin.com/in/maxime/").build();
        userRepository.save(amdinLP);
        userRepository.save(userMaxime);
    }


    private int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }


}
