import { AxiosInstance } from 'axios';

export const fetchMemberInfo = async (axiosInstance: AxiosInstance) => {
    try {
        const response = await axiosInstance.get('/member');
        return response.data;
    } catch (error) {
        //console.error('Fail member info:', error);
        throw error;
    }
};

export const updateRepository = async (axiosInstance: AxiosInstance, repository: string) => {
    try {
        const response = await axiosInstance.patch(`/member?repository=${repository}`);
        return response.data;
    } catch (error) {
        //console.error('Fail:', error);
        throw error;
    }
};