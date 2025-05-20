<template>
  <q-page>
    <div class="q-pa-md">
      <div class="text-h5 text-weight-bold q-mb-md">Inventory</div>

      <q-table
        flat
        bordered
        :rows="rows"
        :columns="columns"
        row-key="id"
        :rows-per-page-options="[0]"
        hide-bottom
        class="q-mb-md"
        :pagination="{
          sortBy: 'name',
          descending: false,
        }"
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
            <q-btn
              unelevated
              text-color="white"
              color="red"
              label="Delete Item"
              no-caps
              class="q-ml-xs"
              @click="deleteItem(props.row)"
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
      </div>
    </div>

    <q-dialog v-model="showItemModal" persistent>
      <q-card style="min-width: 400px">
        <q-card-section class="text-h6">
          {{ mode === 'add' ? 'Add Item' : 'Edit Item' }}
        </q-card-section>

        <q-form ref="formRef">
          <q-card-section class="q-gutter-md">
            <q-input
              v-model="addItemModal.name"
              outlined
              :dense="dense"
              label="Name"
              :rules="[val => !!val || 'Name is required']"
            />
            <q-input
              v-model="addItemModal.description"
              outlined
              :dense="dense"
              label="Description"
              :rules="[val => !!val || 'Description is required']"
            />
            <q-input
              v-model.number="addItemModal.quantity"
              outlined
              :dense="dense"
              label="Quantity"
              type="number"
              :rules="[
                val => val !== null && val !== '' || 'Quantity is required',
                val => !isNaN(val) || 'Must be a number',
                val => val >= 0 || 'Quantity cannot be less than zero'
              ]"
            />
            <q-input
              v-model="addItemModal.category"
              outlined
              :dense="dense"
              label="Category"
              :rules="[val => !!val || 'Category is required']"
            />
          </q-card-section>
        </q-form>

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
  import { ref, reactive, onMounted } from 'vue'
  import { useQuasar } from 'quasar'
  import InventoryAPI from 'src/services/InventoryAPI'

  const $q = useQuasar()
  const showItemModal = ref(false)
  const mode = ref('add')
  const formRef = ref(null);

  const columns = [
    { name: 'name', label: 'Name', align: 'left', field: row => row.name, sortable: true },
    { name: 'description', label: 'Description', align: 'left', field: row => row.description, sortable: true },
    { name: 'quantity', label: 'Quantity', align: 'left', field: row => row.quantity, sortable: true },
    { name: 'category', label: 'Category', align: 'left', field: row => row.category, sortable: true },
    { name: 'actions', label: '', align: 'center', field: 'actions', sortable: false }
  ]

  const rows = ref([])

  onMounted(async () => {
    try {
      const response = await InventoryAPI.getAllInventory();
      rows.value = response.data;
    } catch (error) {
      console.error('ERROR! NO DATA FETCHED!. ' + error.message);
    }
  })

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

  const saveItem = async () => {
    const valid = await formRef.value.validate();

    if (!valid) {
      $q.notify({ type: 'warning', message: 'Please put values to the respective fields', position: 'top' });
      return;
    }

    if (mode.value === 'add') {
      try {
        await InventoryAPI.addInventory({ ...addItemModal });

        rows.value.push({ ...addItemModal }); 
        $q.notify({ type: 'positive', message: 'Item successfully added.', position: 'top' });
      } catch (error) {
        console.error('Item not added! ', error);
      }
    } else {
      try {
        await InventoryAPI.updateInventory(addItemModal.id, { ...addItemModal });
        const index = rows.value.findIndex(item => item.id === addItemModal.id);

        if (index !== -1) {
          rows.value.splice(index, 1, { ...addItemModal });
          $q.notify({ type: 'positive', message: 'Item successfully updated.', position: 'top' });
        }
      } catch (error) {
        console.error('Item not updated! ', error);
      }
    }

    showItemModal.value = false;
    resetModal();
  }

  const deleteItem = (row) => {
    $q.dialog({ title: 'Delete item?', message: 'This action cannot be undone.', cancel: true, ok: { label: 'Delete', color: 'red' }, persistent: true })
      .onOk(async () => {
        try {
          await InventoryAPI.deleteInventory(row.name);

          rows.value = rows.value.filter(item => item.name !== row.name);
          $q.notify({ type: 'positive', message: 'Item successfully deleted.', position: 'top' });
        } catch (error) {
          console.error('Item not deleted! ', error);
        }
      })
};
</script>