package com.bootcamp.desafioquality.common;

import com.bootcamp.desafioquality.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BadRequestAsserter {
    public static void assertBadRequest(ResultActions actions, String message) throws Exception {
        MvcResult mvcResult = actions.andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(message);
    }
}
