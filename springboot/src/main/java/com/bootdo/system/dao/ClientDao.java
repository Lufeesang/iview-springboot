package com.bootdo.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bootdo.system.domain2.Client;

@Mapper
public interface ClientDao {

    public Client createClient(Client client);
    public Client updateClient(Client client);
    public void deleteClient(Long clientId);

    Client findOne(Long clientId);

    List<Client> findAll();

    Client findByClientId(String clientId);
    Client findByClientSecret(String clientSecret);

}
