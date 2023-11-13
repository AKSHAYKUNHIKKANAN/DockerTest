package com.Test.DockerTest.Service;

import com.Test.DockerTest.Model.Address;
import com.Test.DockerTest.Model.Card;

import java.io.IOException;

public interface DockerTestService {

    public String register(String userName,String password,String email);
    public String login(String userName,String password) throws IOException;
    public String postCard(Card card);

    String postAddress(Address address);

    String getAddress(String id);
}
