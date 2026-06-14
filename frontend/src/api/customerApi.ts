import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/customer',
  timeout: 10000,
});

export const customerApi = {
  queryUnfinished(startTime: string, endTime: string) {
    return api.get('/queryUnfinished', {
      params: { startTime, endTime }
    });
  },

  updateAppoint(payload: any) {
    return api.post('/updateAppoint', payload);
  }
};