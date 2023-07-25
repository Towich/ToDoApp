package com.example.todoapp.ui.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.ui.Activity.ui.theme.ToDoAppTheme
import com.example.todoapp.ui.ViewModel.EditTaskViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class EditTaskFragment : Fragment() {

    // ViewModel Factory
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // ViewModel
    private lateinit var viewModel: EditTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject
        (requireContext().applicationContext as App).appComponent.startComponent().create()
            .inject(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[EditTaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                ToDoAppTheme {
                    EditTaskContent()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @Preview(
        showBackground = true,
        showSystemUi = true,
        heightDp = 2000
    )
    @Composable
    fun ToDoAppPreview() {
        ToDoAppTheme {
            EditTaskContent()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTaskContent() {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        var currentImportance by remember { mutableStateOf( "Нет") }
        Scaffold(
            topBar = {
                EditTaskTopBar()
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
//                        .verticalScroll(rememberScrollState())
                    verticalArrangement = Arrangement.Top,
                ) {
                    EditTaskTextField()
                    TextButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier
                            .padding(top = 25.dp, start = 15.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = "Важность",
                                color = MaterialTheme.colorScheme.onSecondary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = currentImportance,
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                    }
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ExtendedFloatingActionButton(
                                    onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                        currentImportance = "Нет"
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .padding(15.dp)
                                ) {
                                    Text(text = "Нет")
                                }

                                ExtendedFloatingActionButton(
                                    onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                        currentImportance = "Низкий"
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .padding(15.dp)
                                ) {
                                    Text(text = "Низкий")
                                }

                                ExtendedFloatingActionButton(
                                    onClick = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                        currentImportance = "Высокийё"
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .padding(15.dp)
                                ) {
                                    Text(text = "Высокий")
                                }
                            }
                        }
                    }
                }
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EditTaskTopBar() { // Top app bar
        TopAppBar(
            title = {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.onSecondary,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        parentFragment?.findNavController()?.navigateUp()
                    },
                    modifier = Modifier.padding(top = 20.dp, start = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close icon",
                        modifier = Modifier
                            .size(35.dp)
                    )
                }
            },
            actions = {

                // Save button
                TextButton(
                    onClick = {

                    },
                    modifier = Modifier.padding(top = 20.dp, end = 10.dp)
                ) {
//                            Icon(
//                                imageVector = Icons.Filled.Done,
//                                contentDescription = "Done icon",
//                                tint = MaterialTheme.colorScheme.onSecondary,
//                                modifier = Modifier
//                                    .size(35.dp)
//                            )
                    Text(
                        text = "СОХРАНИТЬ",
                        color = colorResource(id = R.color.blue),
                        fontSize = 20.sp
                    )
                }
            },
            windowInsets = WindowInsets(
                top = 0.dp,
                bottom = 0.dp
            )
        )
    }

    @Composable
    fun EditTaskTextField() {
        var value by remember {
            mutableStateOf("")
        }
        // Text Field
        BasicTextField(
            value = value,
            onValueChange = { newText ->
                value = newText
            },
            modifier = Modifier
                .padding(top = 20.dp)
                .verticalScroll(ScrollState(0), false),
//                        textStyle = TextStyle(
//                            color = MaterialTheme.colorScheme.onSecondary,
//                            fontSize = MaterialTheme.typography.bodyLarge
//                        ),
            textStyle = MaterialTheme.typography.bodyLarge,
            minLines = 8,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp) // margin left and right
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .padding(all = 16.dp), // inner padding

                ) {
                    innerTextField()
                }
            }
        )
    }


}