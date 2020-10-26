package com.polytech.mtonairserver.controller;

import com.polytech.mtonairserver.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

// todo : classe à implémenter

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
//@ContextConfiguration(classes = SpringTestConfig.class)
public class UserControllerTest {

    @MockBean
    private UserController userController;

   // @Autowired
   // private MockMvc mockMvc;

    // @InjectMocks
    // private UserEntityRepository userEntityRepository = (UserEntityRepository) new UserController();

    @Test
    public void testGetAllUsers() throws Exception {

      /*  List<UserEntity> userList = new ArrayList<>();
        UserEntity userEntity1 = new UserEntity(1, "Myriam", "Benali", "myriam.benali@etudiant.com",
                "12345678", "kdsnfmldfgkblnklnbhf");
        UserEntity userEntity2 = new UserEntity(2, "Dorian", "Naaji", "dorian.naaji@etudiant.com",
                "azertyuiop", "dlfkhm:g,lkhgnlhhhgf");

        userList.add(userEntity1);
        userList.add(userEntity2);
        when(userController.listOfUsers()).thenReturn(userList);
        assertEquals(userController.listOfUsers(), userList);
        Mockito.verify(userController).listOfUsers(); */
    }


    @Test
    public void testFindById() {


        /*UserEntity userEntity = new UserEntity(3, "Lisa", "Lola", "lisa.lola@gmail.com", "abecdeurof", "hdsbjhce");
        when(userController.getAUser(3)).thenReturn(userEntity);

        UserEntity userEntityReturned = userEntityRepository.findByIdUser(3);
        assertEquals(userEntity, userEntityReturned);*/
    }

}
