package estudo.springboot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import estudo.springboot.model.Pessoa;
import estudo.springboot.model.Telefone;
import estudo.springboot.repository.PessoaRepository;
import estudo.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
	TelefoneRepository telefoneRepository;
	

	@RequestMapping(method = RequestMethod.GET, value = ("/cadastropessoa"))
	public ModelAndView inicio() {
		ModelAndView andView = new ModelAndView("/cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIT = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIT);
		andView.addObject("pessoaObj", new Pessoa());
		return andView;
	}

	@PostMapping("/**/salvarpessoa")
	public ModelAndView salvarpessoa(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		ModelAndView andView = new ModelAndView("/cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIT = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIT);
		andView.addObject("pessoaObj", new Pessoa());
		return andView;
	}

	@GetMapping(("/listapessoas"))
	public ModelAndView listaPessoas() {
		ModelAndView andView = new ModelAndView("/cadastro/cadastropessoa");
		Iterable<Pessoa> pessoaIT = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoaIT);
		andView.addObject("pessoaObj", new Pessoa());
		return andView;
	}
	
	
	@GetMapping(value = "/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		
		ModelAndView andView = new ModelAndView("/cadastro/telefones");
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		andView.addObject("pessoaObj", pessoa.get());
		return andView;
	}

	@GetMapping(value = "/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView andView = new ModelAndView("/cadastro/cadastropessoa");
		andView.addObject("pessoaObj", pessoa.get());
		return andView;

	}
	
	@PostMapping(value = "/addFonePessoa/{pessoaid}")
	public ModelAndView addTelefonePessoa(@PathVariable("pessoaid") Long pessoaid, Telefone telefone) {
	    ModelAndView andView = new ModelAndView("/cadastro/telefones");

	    // Verifique se a pessoa existe no banco
	    Pessoa pessoa = pessoaRepository.findById(pessoaid)
	        .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com ID: " + pessoaid));

	    // Associe o telefone à pessoa
	    telefone.setPessoa(pessoa);
	    telefoneRepository.save(telefone);

	    // Adicione os dados ao modelo
	    andView.addObject("pessoaObj", pessoa);
	
	    return andView;
	}

	

	@GetMapping(value = "/excluirpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

		pessoaRepository.deleteById(idpessoa);
		ModelAndView andView = new ModelAndView("/cadastro/cadastropessoa");
		andView.addObject("pessoas", pessoaRepository.findAll());
		andView.addObject("pessoaObj", new Pessoa());
		return andView;

	}

	@PostMapping(value = "**/buscarpessoa")
	public ModelAndView buscarNomePessoa(@RequestParam("nomePesquisa") String nomePesquisa) {
		ModelAndView andView = new ModelAndView("/cadastro/cadastropessoa");

		List<Pessoa> listaPessoasEncontradas = pessoaRepository.findPessoaByName(nomePesquisa);

		if (listaPessoasEncontradas.isEmpty()) {
			andView.addObject("mensagemErro", "Nenhuma pessoa encontrada com o nome: " + nomePesquisa);

		} else {
			andView.addObject("pessoas", listaPessoasEncontradas);
		}

		andView.addObject("pessoaObj", new Pessoa());
		return andView;

	}

}


