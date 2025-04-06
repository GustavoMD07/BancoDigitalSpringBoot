package br.com.cdb.bancodigitalJPA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.cdb.bancodigitalJPA.DTO.SeguroDTO;
import br.com.cdb.bancodigitalJPA.DTO.SeguroResponse;
import br.com.cdb.bancodigitalJPA.service.SeguroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/seguros")
public class SeguroController {

	@Autowired
    private SeguroService seguroService;
	
	@PostMapping
    public ResponseEntity<SeguroResponse> contratarSeguro(@RequestBody @Valid SeguroDTO dto) {
        SeguroResponse response = seguroService.contratarSeguro(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguroResponse> pegarSeguro(@PathVariable Long id) {
        SeguroResponse response = seguroService.getSeguro(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SeguroResponse>> listarSeguros() {
        List<SeguroResponse> lista = seguroService.listarSeguros();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSeguro(@PathVariable Long id) {
        seguroService.cancelarSeguro(id);
        return ResponseEntity.noContent().build();
    }
}
