package com.polytech.mtonairserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.mtonairserver.customexceptions.accountcreation.NamesMissingException;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class that handles some generic testing operations, to avoid redundant code.
 */
public class TestUtils
{
    private static ObjectMapper om = new ObjectMapper();

    /**
     * Performs a post with the given url and json param (param is serialized in json format).
     * @param url api endpoint.
     * @param param the object that will be serialized into a json so it
     * @param mvc
     * @return
     * @throws Exception
     */
    public static ResultActions doPost(String url, Object param, MockMvc mvc) throws Exception
    {
        return mvc.perform(post(url)
                .content(om.writeValueAsString(param))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public static ResultActions doGet(String url, Object param, MockMvc mvc)
    {
        throw new NotImplementedException();
    }

    public static ResultActions doGet(String url, MockMvc mvc)
    {
        throw new NotImplementedException();
    }


}
