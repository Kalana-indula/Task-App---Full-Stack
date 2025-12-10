import { useState } from "react";
import * as React from "react";
import type { AddTaskDto } from "../types";

interface AddTaskCardProps {
    onSubmit: (payload: AddTaskDto) => void;
}

const AddTaskCard = ({ onSubmit }: AddTaskCardProps) => {
    const [taskTitle, setTaskTitle] = useState<string>("");
    const [taskDescription, setTaskDescription] = useState<string>("");

    const handleTaskTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTaskTitle(e.target.value);
    };

    const handleTaskDescription = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setTaskDescription(e.target.value);
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const payload: AddTaskDto = {
            title: taskTitle,
            description: taskDescription,
        };

        onSubmit(payload);

        // clear form
        setTaskTitle("");
        setTaskDescription("");
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-6 mb-6">
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label
                        htmlFor="title"
                        className="block text-sm font-medium text-gray-700 mb-2"
                    >
                        Task Title
                    </label>
                    <input
                        id="title"
                        name="title"
                        type="text"
                        placeholder="e.g., Design the new dashboard"
                        value={taskTitle}
                        required={true}
                        onChange={handleTaskTitle}
                        className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:border-transparent"
                    />
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="description"
                        className="block text-sm font-medium text-gray-700 mb-2"
                    >
                        Description
                    </label>
                    <textarea
                        id="description"
                        name="description"
                        placeholder="Add more details about your task..."
                        value={taskDescription}
                        required={true}
                        onChange={handleTaskDescription}
                        rows={4}
                        className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:border-transparent resize-none"
                    />
                </div>
                <div className="flex justify-end">
                    <button
                        type="submit"
                        className="w-full md:w-auto bg-yellow-400 hover:bg-yellow-500 text-black font-semibold px-6 py-2 rounded-md transition-colors duration-200 hover:cursor-pointer"
                    >
                        Add Task
                    </button>
                </div>
            </form>
        </div>
    );
};

export default AddTaskCard;
