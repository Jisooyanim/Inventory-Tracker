import API from "./API"

export default {
    getAllInventory() {
        return API().get('')
    },

    addInventory(data) {
        return API().post('', data)
    },

    updateInventory(id, data) {
        return API().put(`/${id}`, data)
    },
}