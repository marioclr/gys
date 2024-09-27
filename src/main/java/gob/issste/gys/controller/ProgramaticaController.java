package gob.issste.gys.controller;

import gob.issste.gys.model.Programatica;
import gob.issste.gys.repository.IProgramaticaRepository;
import gob.issste.gys.response.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ProgramaticaController {
    @Autowired
     IProgramaticaRepository iProgramaticaRepository;
    @Operation(summary = "Trae la programatica presupuestal usando valores de paginador como referencia",
            description = "Método que trae programatica presupuestal",
            tags = { "Programatica presupuestal" })
    @GetMapping("/Programatica")
    public ResponseEntity<Object> getAllProgramPresup(@RequestParam int page, int size){
        try {
            Map<String, Object> dataAndSize = new HashMap<>();
            List<Programatica> programaticaList = new ArrayList<>();
           programaticaList = iProgramaticaRepository.findAllProgramatica(page, size);
           dataAndSize.put("programatica",programaticaList);
           dataAndSize.put("size", iProgramaticaRepository.getProgramSize());
            return ResponseHandler.generateResponse("Programatica obtenida con exito", HttpStatus.OK, dataAndSize);
        }catch (Exception e){
            return ResponseHandler.generateResponse("No se pudo obtener la programatrica presupuestal " + e, HttpStatus.CONFLICT, null);
        }
    }

    @Operation(summary = "Trae la programatica presupuestal usando valores de paginador como referencia",
            description = "Método que trae programatica presupuestal",
            tags = { "Programatica presupuestal" })
    @GetMapping("/Programatica/byType")
    public ResponseEntity<Object> getProgramPresupByType(@RequestParam String type, int page, int size){
        String message = "";
        try {
            Map<String, Object> dataAndSize = new HashMap<>();
            List<Programatica> programaticaList = new ArrayList<>();
            programaticaList = iProgramaticaRepository.findAllProgramaticaByType(type,page, size);
            dataAndSize.put("programatica",programaticaList);
            dataAndSize.put("size", iProgramaticaRepository.getProgramSize());

            return ResponseHandler.generateResponse("Programatica de obtenida con exito", HttpStatus.OK, dataAndSize);
        }catch (Exception e){
            return ResponseHandler.generateResponse("No se pudo obtener la programatrica presupuestal " + e, HttpStatus.CONFLICT, null);
        }
    }
    @PutMapping("/Programatica")
    public ResponseEntity<Object> updateProgramaticaById(@RequestBody Programatica programatica){
        try {
            iProgramaticaRepository.upadteProgramatica(programatica);
            return ResponseHandler.generateResponse("Elemento " + programatica.getRowid() + " actualizado con exito", HttpStatus.OK, null);
        }   catch (Exception e) {
            return ResponseHandler.generateResponse("Elemento no actualizado", HttpStatus.CONFLICT, null);
        }
    }
}
