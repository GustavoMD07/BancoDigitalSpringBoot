package br.com.cdb.bancodigitalJPA.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	@PostMapping("/add")
    public ResponseEntity<String> contratarSeguro(@RequestBody @Valid SeguroDTO dto) {
        SeguroResponse response = seguroService.contratarSeguro(dto);
        
        if(response != null) {
        	 return new ResponseEntity<>("Seguro " + response.getTipoDeSeguro() +" contratado com sucesso!", HttpStatus.OK);
        }
        
        else {
			return new ResponseEntity<>("Seguro não foi contratado, erro", HttpStatus.NOT_ACCEPTABLE);
		}
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<SeguroResponse> buscarSeguroPorId(@PathVariable Long id) {
        SeguroResponse response = seguroService.buscarSeguroPorId(id);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<SeguroResponse>> listarSeguros() {
        List<SeguroResponse> lista = seguroService.listarSeguros();
        return new ResponseEntity<>(lista, HttpStatus.FOUND);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarSeguro(@PathVariable Long id) {
        seguroService.cancelarSeguro(id);
        return new ResponseEntity<>("Seguro cancelado com sucesso, obrigado por utilizar nossos serviços.", 
        		HttpStatus.OK);
    }
}
