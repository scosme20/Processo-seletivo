<template>
  <div id="app">
    <SearchIntuitive />
  </div>
</template>

<script>
import SearchIntuitive from './components/searchIntuitive.vue';

export default {
  name: 'App',
  components: {
    SearchIntuitive
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
