<template>
  <div>
    <input v-model="query" placeholder="Digite o nome da operadora" />
    <button @click="buscarOperadora" :disabled="carregando">
      {{ carregando ? "Buscando..." : "Buscar" }}
    </button>

    <ul v-if="operadoras.length > 0">
      <li v-for="operadora in operadoras" :key="operadora.Registro_ANS">
        <strong>{{ operadora.Nome_Fantasia }}</strong> - CNPJ: {{ operadora.CNPJ }}
      </li>
    </ul>
    
    <p v-else-if="!carregando && buscaRealizada">Nenhuma operadora encontrada.</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      query: "",
      operadoras: [],
      carregando: false,
      buscaRealizada: false,
    };
  },
  methods: {
    async buscarOperadora() {
      if (this.query.trim() === "") {
        alert("Por favor, insira um termo de busca.");
        return;
      }

      this.carregando = true;
      this.buscaRealizada = true;

      try {
        const resposta = await axios.get(`https://processo-seletivo-imbt.onrender.com/search?query=${this.query}`);
        this.operadoras = resposta.data;
      } catch (erro) {
        console.error("Erro ao buscar operadoras:", erro);
        alert("Erro ao buscar operadoras. Verifique o console para mais detalhes.");
      } finally {
        this.carregando = false;
      }
    },
  },
};
</script>

<style scoped>
input {
  margin-right: 10px;
  padding: 5px;
}
button {
  padding: 5px 10px;
  cursor: pointer;
}
button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  margin: 5px 0;
  padding: 10px;
  border: 1px solid #ddd;
  background: #f9f9f9;
}
</style>
