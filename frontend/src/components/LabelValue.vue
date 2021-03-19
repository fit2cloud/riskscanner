<template>
  <div class="label-value">
    <label>{{ label }}</label>
    <div>
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "LabelValue",
  props: {
    label: String,
    value: String,
  }
}
</script>

<style scoped>
.label-value {
  display: flex;
  justify-content: flex-start;
}

.label-value label {
  margin: 0 20px;
  text-align: left;
  color: #909090;
  width: 120px;
  flex-grow: 0;
  flex-shrink: 0;
}
</style>
