package com.example.powerlogger.services;

import com.example.powerlogger.Env;
import com.example.powerlogger.dto.GroupDTO;
import com.example.powerlogger.repositories.UserRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupDataService {
    @GET(Env.BASE_API_PREFIX + "/groups/")
    Call<List<GroupDTO>> fetchAllGroups(@Header("Authorization") String token);

    @GET(Env.BASE_API_PREFIX + "/groups/{id}")
    Call<GroupDTO> fetchOneGroup(@Header("Authorization") String token, @Path("id") int id);

    @POST(Env.BASE_API_PREFIX + "/groups")
    Call<GroupDTO> postNewGroup(@Header("Authorization") String token, @Body GroupDTO group);

    @PUT(Env.BASE_API_PREFIX + "/groups/{id}")
    Call<GroupDTO> updateGroup(@Header("Authorization") String token, @Path("id") String id, @Body GroupDTO group);
}
