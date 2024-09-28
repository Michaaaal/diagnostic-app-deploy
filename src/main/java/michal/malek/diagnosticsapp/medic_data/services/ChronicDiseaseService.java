package michal.malek.diagnosticsapp.medic_data.services;

import lombok.RequiredArgsConstructor;
import michal.malek.diagnosticsapp.medic_data.models.ChronicDisease;
import michal.malek.diagnosticsapp.medic_data.models.Drug;
import michal.malek.diagnosticsapp.medic_data.repositories.ChronicDiseaseRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChronicDiseaseService {
    private final ChronicDiseaseRepository chronicDiseaseRepository;

    @Value("classpath:/other/chronic_disease.txt")
    private Resource diseasesResource;

    public void updateChronicDiseases() {
        try (InputStream inputStream = diseasesResource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (chronicDiseaseRepository.findByName(line) == null) {
                    ChronicDisease chronicDisease = new ChronicDisease(line);
                    chronicDiseaseRepository.saveAndFlush(chronicDisease);
                    System.out.println(line + " added ");
                } else {
                    System.out.println(line + " already exists ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading chronic diseases file", e);
        }
    }


    public Stream<ChronicDisease> findChronicDisease(String str) {
        List<ChronicDisease> allByNameContainingIgnoreCase = chronicDiseaseRepository.findAllByNameContainingIgnoreCase(str);
        return allByNameContainingIgnoreCase.stream().sorted(new Comparator<ChronicDisease>() {
            LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
            @Override
            public int compare(ChronicDisease o1, ChronicDisease o2) {

                String query = str.toLowerCase();
                int o1Distance = levenshteinDistance.apply(o1.getName().toLowerCase() , query);
                int o2Distance = levenshteinDistance.apply(o2.getName().toLowerCase() ,query);

                return o1Distance - o2Distance;
            }
        }).limit(20);
    }
}
