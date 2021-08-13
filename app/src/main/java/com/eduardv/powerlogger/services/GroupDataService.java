package com.eduardv.powerlogger.services;

import com.eduardv.powerlogger.Env;
import com.eduardv.powerlogger.dto.GroupDTO;
import com.eduardv.powerlogger.dto.groups.GroupAddExercisesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupDataService {

    String AUTHORIZATION = "Authorization";

    @GET(Env.BASE_API_PREFIX + "/users/{username}/groups")
    Call<List<GroupDTO>> fetchAllGroups(@Path("username") String username, @Header(AUTHORIZATION) String token);

    @GET(Env.BASE_API_PREFIX + "/users/{username}/groups/{id}")
    Call<GroupDTO> fetchOneGroup(@Path("username") String username, @Header(AUTHORIZATION) String token, @Path("id") int id);

    @POST(Env.BASE_API_PREFIX + "/users/{username}/groups")
    Call<GroupDTO> postNewGroup(@Path("username") String username, @Header(AUTHORIZATION) String token, @Body GroupDTO group);

    @PUT(Env.BASE_API_PREFIX + "/users/{username}/groups/{id}")
    Call<GroupDTO> updateGroup(@Path("username") String username, @Header(AUTHORIZATION) String token, @Path("id") String id, @Body GroupDTO group);

    @POST(Env.BASE_API_PREFIX + "/users/{username}/groups/{id}/exercises")
    Call<GroupAddExercisesResponse> addExercises(@Path("username") String username, @Header(AUTHORIZATION) String token, @Path("id") String id, @Body List<String> exercisesId);

    @DELETE(Env.BASE_API_PREFIX + "/groups/{groupId}/exercises/{exerciseId}")
    Call<GroupDTO> removeExerciseFromGroup(@Path("username") String username, @Header(AUTHORIZATION) String token, @Path("groupId") String groupId, @Path("exerciseId") String exerciseId);

    @DELETE(Env.BASE_API_PREFIX + "/groups/{groupId}")
    Call<Void> removeGroup(@Path("username") String username, @Header(AUTHORIZATION) String token, @Path("groupId") String groupId);
}
