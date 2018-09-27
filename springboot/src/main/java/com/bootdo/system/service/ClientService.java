package com.bootdo.system.service;

import java.util.List;

import com.bootdo.system.domain2.Client;




public interface ClientService {
	public Client createClient(Client client);
    public Client updateClient(Client client);
    public void deleteClient(Long clientId);

    Client findOne(Long clientId);

    List<Client> findAll();

    Client findByClientId(String clientId);
    boolean findByClientSecret(String clientId,String clientSecret);
}
