import axios from 'axios'

export default (url = 'http://localhost:8080/api/v1/inventory') => {
    return axios.create({
        baseURL: url,
        headers: {
        'Content-Type': 'application/json'
        }
    })
}