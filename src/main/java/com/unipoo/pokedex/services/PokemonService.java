package com.unipoo.pokedex.services;

import com.unipoo.pokedex.models.Pokemon;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Service
public class PokemonService {

    private final String API_URL = "https://pokeapi.co/api/v2/pokemon/";

    // Método que obtiene la información de un Pokémon por su nombre
    public Pokemon getPokemonByName(String name) {
        RestTemplate restTemplate = new RestTemplate();

        // Hacemos la petición HTTP GET a la PokeAPI
        Map<String, Object> response = restTemplate.getForObject(API_URL + name, Map.class);

        if (response == null) {
            return null;
        }

        // Extraemos los datos de la respuesta
        int id = (int) response.get("id");
        String pokemonName = (String) response.get("name");
        int height = (int) response.get("height");
        int weight = (int) response.get("weight");

        // Procesamos los tipos del Pokémon
        List<Map<String, Object>> typesList = (List<Map<String, Object>>) response.get("types");
        List<String> types = typesList.stream()
                .map(type -> (String) ((Map<String, Object>) type.get("type")).get("name"))
                .collect(Collectors.toList());

        // Procesamos las estadísticas del Pokémon
        List<Map<String, Object>> statsList = (List<Map<String, Object>>) response.get("stats");
        Map<String, Integer> stats = statsList.stream()
                .collect(Collectors.toMap(
                        stat -> (String) ((Map<String, Object>) stat.get("stat")).get("name"),
                        stat -> (int) stat.get("base_stat")));

        int baseExperience = (int) response.get("base_experience");

        // Creamos un objeto Pokémon con la información obtenida
        return new Pokemon(id, pokemonName, height, weight, types, stats, baseExperience);
    }
}
