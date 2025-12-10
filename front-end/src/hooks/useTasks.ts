import { useEffect, useState, useCallback } from "react";
import toast from "react-hot-toast";
import type { Task, AddTaskDto } from "../types";
import {
    fetchRecentTasks,
    createTask,
    completeTask as completeTaskRequest,
} from "../services/taskServices";

export const useTasks = () => {
    const [recentTasks, setRecentTasks] = useState<Task[]>([]);
    const [loading, setLoading] = useState(false);

    const loadRecentTasks = useCallback(async () => {
        try {
            setLoading(true);
            const list = await fetchRecentTasks();
            setRecentTasks(list);
        } catch (err) {
            console.log(err);
            setRecentTasks([]);
            toast.error("Failed to load tasks");
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadRecentTasks();
    }, [loadRecentTasks]);

    const addTask = async (payload: AddTaskDto) => {
        try {
            await createTask(payload);
            toast.success("Task added successfully!");
            // reload from backend so we always respect “recent 5 incomplete”
            await loadRecentTasks();
        } catch (err) {
            console.log(err);
            toast.error("Failed to add task");
        }
    };

    const completeTaskAndRemove = async (taskId: number) => {
        try {
            await completeTaskRequest(taskId);
            toast("Good job!", { icon: "👏" });

            await loadRecentTasks();
        } catch (err) {
            console.log(err);
            toast.error("Failed to complete task");
        }
    };

    return {
        recentTasks,
        loading,
        addTask,
        completeTaskAndRemove,
    };
};
