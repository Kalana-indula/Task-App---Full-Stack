import AddTaskCard from "../components/AddTaskCard";
import TaskCard from "../components/TaskCard.tsx";
import { useTasks } from "../hooks/useTasks";
import {ListTodo} from "lucide-react";

const Home = () => {
    const { recentTasks, loading, addTask, completeTaskAndRemove } = useTasks();

    return (
        <div className="min-h-screen bg-gray-100 py-8">
            <div className="max-w-4xl mx-auto px-4">
                <h1 className="text-4xl font-bold text-center text-gray-900 mb-8">
                    To-Do App
                </h1>

                {/* add task card */}
                <div>
                    <AddTaskCard onSubmit={addTask} />
                </div>

                {/* recent tasks list */}
                <div>
                    <h2 className="text-2xl font-bold text-gray-900 mb-6">Active Tasks</h2>

                    {loading ? (
                        <div className="bg-white rounded-lg shadow-md p-6 mb-4 text-center text-gray-500">
                            Loading tasks...
                        </div>
                    ) : recentTasks.length === 0 ? (
                        <div className="bg-white rounded-lg shadow-md p-12 text-center">
                            <div className="flex justify-center mb-4 text-gray-500">
                                <ListTodo strokeWidth={1.25} size={60} />
                            </div>
                            <h3 className="text-xl font-semibold text-gray-900 mb-2">
                                No tasks yet
                            </h3>
                            <p className="text-gray-500">
                                Add your first task using the form above.
                            </p>
                        </div>
                    ) : (
                        <div>
                            {recentTasks.map((task) => (
                                <div key={task.id}>
                                    <TaskCard
                                        task={task}
                                        onCompleted={completeTaskAndRemove}
                                    />
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Home;
