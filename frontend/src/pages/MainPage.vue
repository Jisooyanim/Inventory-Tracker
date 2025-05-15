<template>
  <q-page>
    <div class="q-pa-md">
      <div class="text-h5 text-weight-bold q-mb-md">Inventory</div>

      <q-table
        flat
        bordered
        :rows="rows"
        :columns="columns"
        row-key="name"
        hide-bottom
        class="q-mb-md"
      >
        <template v-slot:body-cell-actions="props">
          <q-td align="center">
            <q-btn
              unelevated
              text-color="black"
              label="Edit"
              no-caps
              class="edit-outline"
              @click="editItem(props.row)"
            />
          </q-td>
        </template>
      </q-table>

      <div>
        <q-btn
          unelevated
          text-color="black"
          label="Add Item"
          no-caps
          class="edit-outline"
          @click="openAddModal"
        />
        <q-btn
          unelevated
          text-color="white"
          color="red"
          label="Delete Item"
          no-caps
          class="q-ml-xs"
        />
      </div>
    </div>

    <q-dialog v-model="showItemModal" persistent>
      <q-card style="min-width: 400px">
        <q-card-section class="text-h6">
          {{ mode === 'add' ? 'Add Item' : 'Edit Item' }}
        </q-card-section>

        <q-card-section class="q-gutter-md">
          <q-input v-model="addItemModal.name" outlined :dense="dense" label="Name" />
          <q-input v-model="addItemModal.description" outlined :dense="dense" label="Description" />
          <q-input v-model.number="addItemModal.quantity" outlined :dense="dense" label="Quantity" type="number" />
          <q-input v-model="addItemModal.category" outlined :dense="dense" label="Category" />
        </q-card-section>

        <q-card-actions class="q-px-md q-pt-none q-pb-md justify-end">
          <q-btn
            label="Cancel"
            no-caps
            unelevated
            text-color="black"
            class="gray-outline-btn min-btn"
            v-close-popup
          />
          <q-btn
            label="Save"
            no-caps
            unelevated
            color="blue-10"
            text-color="white"
            class="min-btn"
            @click="saveItem"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup>
  import { ref, reactive } from 'vue'
  import { useQuasar } from 'quasar'

  const $q = useQuasar()
  const showItemModal = ref(false)
  const mode = ref('add')

  const columns = [
    { name: 'name', label: 'Name', align: 'left', field: row => row.name, sortable: true },
    { name: 'description', label: 'Description', align: 'left', field: row => row.description, sortable: true },
    { name: 'quantity', label: 'Quantity', align: 'left', field: row => row.quantity, sortable: true },
    { name: 'category', label: 'Category', align: 'left', field: row => row.category, sortable: true },
    { name: 'actions', label: '', align: 'center', field: 'actions', sortable: false }
  ]

  const rows = ref([
    {
      name: "Laptop",
      description: "15-inch display",
      quantity: 10,
      category: "Electronics"
    }
  ])

  const addItemModalDefault = {
    name: '',
    description: '',
    quantity: null,
    category: ''
  }

  const addItemModal = reactive({ ...addItemModalDefault })

  const resetModal = () => Object.assign(addItemModal, addItemModalDefault)

  const openAddModal = () => {
    mode.value = 'add'
    resetModal()
    showItemModal.value = true
  }

  const editItem = row => {
    mode.value = 'edit'
    Object.assign(addItemModal, row)
    showItemModal.value = true
  }

  const saveItem = () => {
    if (mode.value === 'add') {
      rows.value.push({ ...addItemModal })
      $q.notify({ type: 'positive', message: 'Item successfully added.', position: 'top' })
    } else {
      const index = rows.value.findIndex(item => item.name === addItemModal.name)
      if (index !== -1) {
        rows.value[index] = { ...addItemModal }
        $q.notify({ type: 'positive', message: 'Item successfully updated.', position: 'top' })
      }
    }

    showItemModal.value = false
    resetModal()
  }
</script>