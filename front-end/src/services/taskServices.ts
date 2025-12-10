import axios from "axios";
import type { Task, AddTaskDto } from "../types";

interface ListResponse<T> {
    message: string;
    entityList?: T[];
}

const API_BASE = import.meta.env.VITE_API_URL as string;

if (!API_BASE) {
    throw new Error("VITE_API_URL is not defined");
}

export const fetchRecentTasks = async (): Promise<Task[]> => {
    const res = await axios.get<ListResponse<Task>>(`${API_BASE}/tasks/recent`);
    return res.data.entityList ?? [];
};

export const createTask = async (payload: AddTaskDto): Promise<Task> => {
    const res = await axios.post<Task>(`${API_BASE}/tasks`, payload);
    return res.data;
};

export const completeTask = async (id: number): Promise<void> => {
    await axios.put(`${API_BASE}/tasks/${id}/complete`);
};
