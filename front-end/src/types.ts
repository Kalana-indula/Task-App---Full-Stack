export interface Task {
    id: number;
    taskId: string;
    title: string;
    description: string;
    createdAt: string;
    completed: boolean;
}

export interface AddTaskDto {
    title: string;
    description: string;
}
